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
                email: "mr.leonov@mail.ru",
                phoneNumber: "+375292796152",
                password: "\$2a\$12\$kjMvQl359in5BXoX1mgAhORCAwfCU3OREAAR1wX9444WVoMvORKiW"
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
                id: 4
        )
    }
}