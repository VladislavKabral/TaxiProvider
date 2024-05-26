package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/promoCodes")
        method GET()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                content: [
                        [
                            "id": 1,
                            "value": "JAVA",
                            "discount": 0.25
                        ],
                        [
                            "id": 2,
                            "value": "MODSEN",
                            "discount": 0.4
                        ],
                        [
                            "id": 3,
                            "value": "KABRAL",
                            "discount": 0.5
                        ]
                ]
        )
    }
}