package org.apache.mesos.elasticsearch.scheduler.matcher;

import org.apache.mesos.Protos;
import org.apache.mesos.elasticsearch.common.Configuration;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Collection;

/**
 * Matcher for {@link org.apache.mesos.Protos.Request}s
 */
public class RequestMatcher extends BaseMatcher<Collection<Protos.Request>> {

    private double cpus;
    private double mem;
    private double disk;

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(Object o) {
        Collection<Protos.Request> requests = (Collection<Protos.Request>) o;

        Protos.Resource cpuResource = Protos.Resource.newBuilder()
                .setName("cpus")
                .setType(Protos.Value.Type.SCALAR)
                .setScalar(Protos.Value.Scalar.newBuilder().setValue(cpus).build())
                .build();

        Protos.Resource memResource = Protos.Resource.newBuilder()
                .setName("mem")
                .setType(Protos.Value.Type.SCALAR)
                .setScalar(Protos.Value.Scalar.newBuilder().setValue(mem).build())
                .build();

        Protos.Resource diskResource = Protos.Resource.newBuilder()
                .setName("disk")
                .setType(Protos.Value.Type.SCALAR)
                .setScalar(Protos.Value.Scalar.newBuilder().setValue(disk).build())
                .build();

        Protos.Value.Range clientPortRange = Protos.Value.Range.newBuilder().setBegin(Configuration.ELASTICSEARCH_CLIENT_PORT).setEnd(Configuration.ELASTICSEARCH_CLIENT_PORT).build();
        Protos.Value.Range transportPortRange = Protos.Value.Range.newBuilder().setBegin(Configuration.ELASTICSEARCH_TRANSPORT_PORT).setEnd(Configuration.ELASTICSEARCH_TRANSPORT_PORT).build();

        Protos.Resource portsResource = Protos.Resource.newBuilder()
                .setName("ports")
                .setType(Protos.Value.Type.RANGES)
                .setRanges(Protos.Value.Ranges.newBuilder().addRange(clientPortRange).addRange(transportPortRange))
                .build();

        Protos.Request request = Protos.Request.newBuilder()
                .addResources(cpuResource)
                .addResources(memResource)
                .addResources(diskResource)
                .addResources(portsResource)
                .build();

        return requests.contains(request);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(cpus + " cpu(s)");
    }

    public RequestMatcher cpus(double cpus) {
        this.cpus = cpus;
        return this;
    }

    public RequestMatcher mem(double mem) {
        this.mem = mem;
        return this;
    }

    public RequestMatcher disk(double disk) {
        this.disk = disk;
        return this;
    }

}
