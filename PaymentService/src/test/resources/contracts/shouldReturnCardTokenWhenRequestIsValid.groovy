package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/payment/token")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                number: '4242424242424242',
                month: 12,
                year: 2027,
                cvc: 467
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
            token: 'cur_fewfwfwfwfw23fds'
        )
    }
}