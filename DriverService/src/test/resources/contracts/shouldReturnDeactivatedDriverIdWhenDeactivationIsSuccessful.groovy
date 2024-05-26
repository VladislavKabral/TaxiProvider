package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/drivers/1")
        method DELETE()
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