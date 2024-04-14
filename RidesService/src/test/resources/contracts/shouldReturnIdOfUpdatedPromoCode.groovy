package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/promoCodes/1")
        headers {
            contentType applicationJson()
        }
        method PATCH()
        body(
                value: "JAVA",
                discount: "0.4"
        )
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