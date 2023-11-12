package com.example.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.lang.Nullable;

import io.awspring.cloud.core.config.AwsPropertySource;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.imds.Ec2MetadataClient;

public class Ec2InstanceMetadataPropertySource extends AwsPropertySource <Ec2InstanceMetadataPropertySource, Ec2MetadataClient>{

    private final String context;
    private final Ec2MetadataClient client;
    private final Map<String, String> properties = new LinkedHashMap<>();
    private final String prefix = "/latest/meta-data/";


    public Ec2InstanceMetadataPropertySource(String name, Ec2MetadataClient source) {
        super(name, source);
        context = name;
        client = source;
    }

    @Override
    public Ec2InstanceMetadataPropertySource copy() {
        return new Ec2InstanceMetadataPropertySource(context, source);
    }

    @Override
    public void init() {
        if ( !Ec2EnvironmentCheckUtils.isRunningOnCloudEnvironment() ) return;

        mapPut(properties, "ami-id");
        mapPut(properties, "instance-id");
        mapPut(properties, "instance-action");
        mapPut(properties, "instance-type");
        mapPut(properties, "local-ipv4");
        mapPut(properties, "local-hostname");
        mapPut(properties, "hostname");
        mapPut(properties, "public-hostname");
        mapPut(properties, "public-ipv4");
        mapPut(properties, "mac");
        mapPut(properties, "reservation-id");
        mapPut(properties, "security-groups");
        mapPut(properties, "placement/availability-zone");
    }

    // Convenience method to cut down on redundant code.
    private void mapPut(Map<String, String> map, String key) {
        try {
            map.put(key, client.get(prefix +  key).asString());
        } catch (SdkClientException e) {
            logger.debug("Unable to read property " + prefix + key + ", exception message: " + e.getMessage());
        }
    }

    @Override
    public String[] getPropertyNames() {
        return properties.keySet().stream().toArray(String[]::new);
    }

    @Override
    @Nullable
    public Object getProperty(String name) {
        return properties.get(name);
    }
    
}
