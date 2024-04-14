package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/passengers")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                lastname: "Leonov",
                firstname: "Kiril",
                email: "mr.leonovmail.ru",
                phoneNumber: "+375292796152",
                password: "\$2a\$12\$kjMvQl359in5BXoX1mgAhORCAwfCU3OREAAR1wX9444WVoMvORKiW"
        )
    }

    response {
        status BAD_REQUEST()
        headers {
            contentType applicationJson()
        }
        body(
                message: "Wrong email format."
        )
    }
}