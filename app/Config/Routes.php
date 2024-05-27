<?php

use CodeIgniter\Router\RouteCollection;

/**
 * @var RouteCollection $routes
 */
$routes->add('/api/web/v1/send_email', 'Home::testEmail');


$routes->get('/', 'Home::index');
$routes->add('/(:any)', 'Home::$1');
