package com.ceaver.bno.bitcoin

enum class BitcoinService(val bit: Int) {
    NODE_NETWORK(1),
    NODE_GETUTXO(2),
    NODE_BLOOM(4),
    NODE_WITNESS(8),
    NODE_NETWORK_LIMITED(1024);

    companion object {
        fun getServicesAsString(serviceBits: Int): String {
            return values().filter { it.bit and serviceBits != 0 }.joinToString(", ") { it.name }.plus(" ($serviceBits)")
        }
    }
}
