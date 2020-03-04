package domain.requests

trait DomainRequest[R] {

  def toDomain: R

}
