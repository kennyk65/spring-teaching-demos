package com.example;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

/**
 * Simple DTO to make it easy to carry InstanceMetadata to a page for display.
 * 
 */
public class InstanceMetadata {

	Environment env;
	
	public InstanceMetadata( Environment env) {
		this.env = env;
	}

	public void init() {
		amiId = env.getProperty("ami-id");
		amiLaunchIndex = env.getProperty("ami-launch-index");
		amiManifestPath = env.getProperty("ami-manifest-path");	
		hostname = env.getProperty("hostname");
		instanceAction = env.getProperty("instance-action");
		instanceId = env.getProperty("instance-id");
		instanceType = env.getProperty("instance-type");
		kernelId = env.getProperty("kernel-id");
		localHostname = env.getProperty("local-hostname");
		localIpv4 = env.getProperty("local-ipv4");
		mac = env.getProperty("mac");
		publicHostname = env.getProperty("public-hostname");
		publicIpv4 = env.getProperty("public-ipv4");
		reservationId = env.getProperty("reservation-id");
		securityGroups = env.getProperty("security-groups");
		publicKeys = env.getProperty("public-keys");
		availabilityZone = env.getProperty("placement/availability-zone");
	}

	String amiId;
	String amiLaunchIndex;
	String amiManifestPath;
	String hostname;
	String instanceAction;
	String instanceId;
	String instanceType;
	String kernelId;
	String localHostname;
	String localIpv4;
	String mac;
	String publicHostname;
	String publicIpv4;
	String reservationId;
	String securityGroups;
	String publicKeys;
	String availabilityZone;
	
	


	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}	
	
	public String getAmiId() {
		return amiId;
	}

	public void setAmiId(String amiId) {
		this.amiId = amiId;
	}

	public String getAmiLaunchIndex() {
		return amiLaunchIndex;
	}

	public void setAmiLaunchIndex(String amiLaunchIndex) {
		this.amiLaunchIndex = amiLaunchIndex;
	}

	public String getAmiManifestPath() {
		return amiManifestPath;
	}

	public void setAmiManifestPath(String amiManifestPath) {
		this.amiManifestPath = amiManifestPath;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getInstanceAction() {
		return instanceAction;
	}

	public void setInstanceAction(String instanceAction) {
		this.instanceAction = instanceAction;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	public String getKernelId() {
		return kernelId;
	}

	public void setKernelId(String kernelId) {
		this.kernelId = kernelId;
	}

	public String getLocalHostname() {
		return localHostname;
	}

	public void setLocalHostname(String localHostname) {
		this.localHostname = localHostname;
	}

	public String getLocalIpv4() {
		return localIpv4;
	}

	public void setLocalIpv4(String localIpv4) {
		this.localIpv4 = localIpv4;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getPublicHostname() {
		return publicHostname;
	}

	public void setPublicHostname(String publicHostname) {
		this.publicHostname = publicHostname;
	}

	public String getPublicIpv4() {
		return publicIpv4;
	}

	public void setPublicIpv4(String publicIpv4) {
		this.publicIpv4 = publicIpv4;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public String getSecurityGroups() {
		return securityGroups;
	}

	public void setSecurityGroups(String securityGroups) {
		this.securityGroups = securityGroups;
	}

	public String getPublicKeys() {
		return publicKeys;
	}

	public void setPublicKeys(String publicKeys) {
		this.publicKeys = publicKeys;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}
	
//	block-device-mapping/
//	network/
//	placement/
//	services/	
	
}
