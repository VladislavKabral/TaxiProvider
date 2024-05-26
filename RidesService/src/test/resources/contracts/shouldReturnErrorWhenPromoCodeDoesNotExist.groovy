package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/promoCodes?value=Sfewf")
        method GET()
    }

    response {
        status NOT_FOUND()
        headers {
            contentType applicationJson()
        }
        body(
                message: "Promo code 'Sfewf' wasn't found."
        )
    }
}