<?php

namespace App\Controllers;

use Exception;
class Home extends BaseController
{
    public function index(): string
    {
        return view('welcome_message');
    }
    public function appendHeader()
    {
        if (getenv("CI_ENVIRONMENT") == 'development') {
            if ($this->request->getMethod(true) == "OPTIONS") {
                $this->response->appendHeader('Access-Control-Allow-Origin', '*');
                $this->response->appendHeader('Access-Control-Allow-Methods', '*');
                $this->response->appendHeader('Access-Control-Allow-Credentials', 'true');
                $this->response->appendHeader('Access-Control-Allow-Headers', 'Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With');
                $this->response->setJSON(array("success", "okay"));
                $this->response->send();
                exit();
            }
            $this->response->appendHeader("Access-Control-Allow-Origin", "*");
            $this->response->appendHeader("Access-Control-Allow-Methods", "*");
            $this->response->appendHeader("Access-Control-Max-Age", 3600);
            $this->response->appendHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers,Authorization, X-Requested-With");
        }
    }
    public function testEmail()
    {
        $this->appendHeader();
        $input = $this->request->getJSON();
        $subject = $input->subject;
        $message = $input->message;
        $email = $input->email;
        try {
            $this->sendMail($email, $subject, $message);
            return $this->response->setStatusCode(200)->setJSON(array("message" => "working"));

        } catch (Exception $e) {
            return $this->response->setStatusCode(403)->setJSON(array("accessToken" => $e->getMessage()));
        }
       
    }

}
