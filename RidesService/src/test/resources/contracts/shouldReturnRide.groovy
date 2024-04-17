package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/rides/1")
        method GET()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                "id": 1,
                "passengerId": 1,
                "driverId": 1,
                "startedAt": "2024-03-29, 21-50-00",
                "endedAt": "2024-03-29, 22-30-00",
                "sourceAddress": [
                    "lat": 53.1234567,
                    "lon": 90.1234567
                ],
                "destinationAddresses": [
                        [
                            "lat": 53.3456789,
                            "lon": 90.3456789
                        ]
                ],
                "promoCode": [
                        value: "JAVA"
                ],
                "cost": 7.0000,
                "status": "COMPLETED",
                "paymentType": "CARD"
        )
    }
}