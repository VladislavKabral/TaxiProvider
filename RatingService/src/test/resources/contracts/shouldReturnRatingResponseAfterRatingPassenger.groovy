package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/ratings")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                "taxiUserId": 1,
                "role": "PASSENGER",
                "value": 4
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
                taxiUserId: 1,
                role: "PASSENGER",
                value: 4.0
        )
    }
}