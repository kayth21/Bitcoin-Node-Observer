package com.ceaver.bno

import android.content.Context
import androidx.work.*
import com.ceaver.bno.bitnodes.BitnodesRepository
import com.ceaver.bno.extensions.asLocalDateTime
import com.ceaver.bno.snapshots.Snapshot
import com.ceaver.bno.snapshots.SnapshotRepository
import org.greenrobot.eventbus.EventBus

object Workers {

    fun run() {
        WorkManager.getInstance()
            .beginWith(updateSnapshot())
            .then(notifyEnd())
            .enqueue()
    }

    private fun updateSnapshot(): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<SnapshotWorker>().build()
    }

    private fun notifyEnd(): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<EndWorker>().build()
    }

    private const val SNAPSHOT_WORKER_ERROR = "com.ceaver.bno.Workers.SnapshotWorker.error"

    class SnapshotWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            val response = BitnodesRepository.lookupLatestSnapshot()
            return if (response.isSuccessful()) {
                val bitnodesSnapshot = response.result!!
                val snapshot = Snapshot(
                    bitnodesSnapshot.blockHeight,
                    bitnodesSnapshot.totalNodes,
                    bitnodesSnapshot.timestamp.asLocalDateTime()
                )
                SnapshotRepository.update(snapshot)
                Result.success()
            } else {
                Result.success(Data.Builder().putString(SNAPSHOT_WORKER_ERROR, response.error!!).build())
            }
        }
    }

    class EndWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            EventBus.getDefault().post(WorkerEvents.End(inputData.getString(SNAPSHOT_WORKER_ERROR)))
            return Result.success()
        }
    }
}