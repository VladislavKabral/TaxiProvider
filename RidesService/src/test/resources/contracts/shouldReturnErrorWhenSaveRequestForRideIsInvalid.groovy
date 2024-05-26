package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/rides")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                passengerId: 1,
                sourceAddress: null,
                destinationAddresses: [
                    [
                          lat: 53.3456789,
                          lon: 90.3456789
                    ]
                ],
                paymentType: "CARD",
                promoCode: [
                        value: "JAVA"
                ]
        )
    }

    response {
        status BAD_REQUEST()
        headers {
            contentType applicationJson()
        }
        body(
                message: "Source address must be not empty."
        )
    }
}