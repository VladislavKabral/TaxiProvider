package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/ratings?taxiUserId=1&role=PASSENGER")
        method GET()
    }

    response {
        status OK()
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