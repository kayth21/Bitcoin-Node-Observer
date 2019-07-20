package com.ceaver.bno

class WorkerEvents {
    class Start()
    class SnapshotWorkerStart()
    data class SnapshotWorkerEnd(val error: String?)
    class End()
}