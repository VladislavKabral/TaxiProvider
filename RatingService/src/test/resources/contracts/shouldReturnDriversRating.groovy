package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/ratings?taxiUserId=1&role=DRIVER")
        method GET()
    }

    response {
        status OK()
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