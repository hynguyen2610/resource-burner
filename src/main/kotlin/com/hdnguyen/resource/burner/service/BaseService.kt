package com.hdnguyen.resource.burner.service;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseService {
    protected val logger: Logger = LoggerFactory.getLogger(javaClass)
}
