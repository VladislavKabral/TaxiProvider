package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/promoCodes")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                value: "BOOT",
                discount: "0.5"
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
                id: 6
        )
    }
}