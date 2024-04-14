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
                "value": 9
        )
    }

    response {
        status BAD_REQUEST()
        headers {
            contentType applicationJson()
        }
        body(
                message: "Maximum value for the rating's value is '5'."
        )
    }
}