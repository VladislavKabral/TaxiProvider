package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/promoCodes")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                value: "J",
                discount: 0.15
        )
    }

    response {
        status BAD_REQUEST()
        headers {
            contentType applicationJson()
        }
        body(
            message: "Promo code must be between 2 and 50 symbols."
        )
    }
}