package xyz.mommde.synctis.untis.legacy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Implements JsonRPC 2.0

@Serializable
data class GeneralLegacyPacketRequest<T>(
    val id: String,
    val method: String,
    @SerialName("params")
    val parameter: T,
    @SerialName("jsonrpc")
    val jsonRpc: String = "2.0",
)

@Serializable
data class GeneralLegacyPacketResponse<T>(
    val id: String,
    val error: ErrorPacketResponse? = null,
    val result: T? = null,
    @SerialName("jsonrpc")
    val jsonRpc: String = "2.0",
)

@Serializable
data class ErrorPacketResponse(
    val code: Int,
    val message: String,
)