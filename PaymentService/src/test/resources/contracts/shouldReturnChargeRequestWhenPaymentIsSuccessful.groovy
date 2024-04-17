package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/payment/charge")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                amount: 10.0,
                currency: "USD",
                cardToken: 'cur_fewfwfwfwfw23fds',
                passengerId: 1,
                driverId: 1
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                amount: 10.0,
                currency: 'USD',
                status: 'SUCCEED',
        )
    }
}