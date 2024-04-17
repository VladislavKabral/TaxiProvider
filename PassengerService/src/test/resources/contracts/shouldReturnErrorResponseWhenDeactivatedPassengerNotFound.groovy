package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/passengers/4")
        headers {
            contentType applicationJson()
        }
        method DELETE()
    }

    response {
        status NOT_FOUND()
        headers {
            contentType applicationJson()
        }
        body(
                message: "Passenger with id '4' wasn't found."
        )
    }
}