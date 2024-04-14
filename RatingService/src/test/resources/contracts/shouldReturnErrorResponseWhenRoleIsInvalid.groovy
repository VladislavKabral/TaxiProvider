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
                "role": "GJHfewew4",
                "value": 4
        )
    }

    response {
        status BAD_REQUEST()
        headers {
            contentType applicationJson()
        }
        body(
               message: "GJHfewew4 is wrong role name."
        )
    }
}