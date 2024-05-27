<?php

namespace App\Controllers;

use CodeIgniter\Controller;
use CodeIgniter\HTTP\CLIRequest;
use CodeIgniter\HTTP\IncomingRequest;
use CodeIgniter\HTTP\RequestInterface;
use CodeIgniter\HTTP\ResponseInterface;
use Psr\Log\LoggerInterface;

/**
 * Class BaseController
 *
 * BaseController provides a convenient place for loading components
 * and performing functions that are needed by all your controllers.
 * Extend this class in any new controllers:
 *     class Home extends BaseController
 *
 * For security be sure to declare any new methods as protected or private.
 */
abstract class BaseController extends Controller
{
    /**
     * Instance of the main Request object.
     *
     * @var CLIRequest|IncomingRequest
     */
    protected $request;

    /**
     * An array of helpers to be loaded automatically upon
     * class instantiation. These helpers will be available
     * to all other controllers that extend BaseController.
     *
     * @var list<string>
     */
    protected $helpers = [];

    /**
     * Be sure to declare properties for any property fetch you initialized.
     * The creation of dynamic property is deprecated in PHP 8.2.
     */
    // protected $session;

    /**
     * @return void
     */
    public function initController(RequestInterface $request, ResponseInterface $response, LoggerInterface $logger)
    {
        // Do Not Edit This Line
        parent::initController($request, $response, $logger);

        // Preload any models, libraries, etc, here.

        // E.g.: $this->session = \Config\Services::session();
    }

    /**
     * Send email
     * @param string $email Destination email address
     * @param string $subject Email subject
     * @param string $msg Email body, it may contains html codes
     * @param String $institution
     * @return bool
     * @throws \Exception
     */
    public function sendMail(string $email, string $subject, string $msg, String $institution = 'Padri Vjeko Centre - WEBSITE CONTACT US'): bool
    {
        $email1 = \Config\Services::email();
        $config = array(
            "SMTPHost" => "mail.qonics.com", "SMTPUser" => "padrivjekotss@qonics.com", "SMTPPass" => "9MNa3Vm065RQ", "protocol" => "smtp", "SMTPPort" => 587, "mailType" => "html"
        );
        $email1->initialize($config);
        $email1->setFrom($email, "$institution");
        $email1->setTo("dallyjones6@gmail.com");
        $email1->setSubject($subject);
        $email1->setMessage($msg);
        if ($email1->send(false)) {
            return true;
        }
        //      var_dump($this->email->printDebugger());
        log_message('critical', 'email-issue: ' . json_encode($email1->printDebugger()));
        throw new \Exception("System failed to send email.", 400);
    }
}
