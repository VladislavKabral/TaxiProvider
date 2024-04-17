package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/rides/1")
        headers {
            contentType applicationJson()
        }
        method PATCH()
        body(
                id: 1,
                passengerId: 1,
                driverId: 1,
                cost: 7.0,
                promoCode: [
                        value: "JAVA"
                ],
                status: "COMPLETED",
                paymentType: "CARD"
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