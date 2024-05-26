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
             "role": "PASSENGER"
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
                value: 5.0
        )
    }
}