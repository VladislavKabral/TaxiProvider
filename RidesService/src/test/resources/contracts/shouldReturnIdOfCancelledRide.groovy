package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/rides/1")
        method PATCH()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
            id: 1
        )
    }
}