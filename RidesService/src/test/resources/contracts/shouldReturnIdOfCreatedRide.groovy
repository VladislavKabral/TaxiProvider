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
                sourceAddress: [
                    lat: 53.1234567,
                    lon: 90.1234567
                ],
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
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
            id: 6
        )
    }
}