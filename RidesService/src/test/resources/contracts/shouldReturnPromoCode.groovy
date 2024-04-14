package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/promoCodes?value=JAVA")
        method GET()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                id: 1,
                value: "JAVA",
                discount: 0.25
        )
    }
}