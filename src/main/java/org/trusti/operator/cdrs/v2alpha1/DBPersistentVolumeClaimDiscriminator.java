package org.trusti.operator.cdrs.v2alpha1;

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ResourceDiscriminator;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import io.javaoperatorsdk.operator.processing.event.source.informer.InformerEventSource;

import java.util.Optional;

public class DBPersistentVolumeClaimDiscriminator implements ResourceDiscriminator<PersistentVolumeClaim, Trusti> {
    @Override
    public Optional<PersistentVolumeClaim> distinguish(Class<PersistentVolumeClaim> resource, Trusti trusti, Context<Trusti> context) {
        String persistentVolumeClaimName = DBPersistentVolumeClaim.getPersistentVolumeClaimName(trusti);
        ResourceID resourceID = new ResourceID(persistentVolumeClaimName, trusti.getMetadata().getNamespace());
        var informerEventSource = (InformerEventSource<PersistentVolumeClaim, Trusti>) context.eventSourceRetriever().getResourceEventSourceFor(PersistentVolumeClaim.class);
        return informerEventSource.get(resourceID);
    }
}
