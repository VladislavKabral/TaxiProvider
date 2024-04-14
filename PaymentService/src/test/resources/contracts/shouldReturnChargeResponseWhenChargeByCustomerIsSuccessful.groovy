package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/payment/customerCharge")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                taxiUserId: 1,
                amount: 10.0,
                currency: 'USD',
                role: 'PASSENGER'
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                currency: 'USD',
                amount: 10.0,
                status: 'SUCCEED'
        )
    }
}