package services

import java.util.UUID

object RequestIdService {
  def get: Long = UUID.randomUUID().getMostSignificantBits
}
