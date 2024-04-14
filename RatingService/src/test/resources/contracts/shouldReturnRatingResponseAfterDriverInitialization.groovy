package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/ratings/init")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                "taxiUserId": 1,
                "role": "DRIVER"
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
                taxiUserId: 1,
                role: "DRIVER",
                value: 5.0
        )
    }
}