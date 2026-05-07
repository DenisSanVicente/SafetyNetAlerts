package com.safetynetalerts.SafetyNetAlerts;

import com.safetynetalerts.SafetyNetAlerts.model.FireStation;
import com.safetynetalerts.SafetyNetAlerts.service.FireStationService;
import com.safetynetalerts.SafetyNetAlerts.service.FireStationServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SafetyNetAlertsApplication {

	public static void main(String[] args) {

        SpringApplication.run(SafetyNetAlertsApplication.class, args);

	}

}
