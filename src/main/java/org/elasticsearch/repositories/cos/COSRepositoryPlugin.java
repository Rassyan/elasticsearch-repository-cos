package org.elasticsearch.repositories.cos;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.elasticsearch.cluster.metadata.RepositoryMetadata;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.repositories.RepositoriesMetrics;
import org.elasticsearch.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.Environment;
import org.elasticsearch.indices.recovery.RecoverySettings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.RepositoryPlugin;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.repositories.Repository;

/**
 * Created by Ethan-Zhang on 30/03/2018.
 */
public class COSRepositoryPlugin extends Plugin implements RepositoryPlugin {

    static {
        Logger.getLogger("org.apache.http.wire").setLevel(Level.INFO);
    }
    protected COSService createStorageService(RepositoryMetadata metaData) {
        return new COSService(metaData);
    }

    @Override
    public Map<String, Repository.Factory> getRepositories(final Environment env,
                                                           final NamedXContentRegistry namedXContentRegistry,
                                                           final ClusterService clusterService,
                                                           final BigArrays bigArrays,
                                                           final RecoverySettings recoverySettings,
                                                           final RepositoriesMetrics repositoriesMetrics) {
        return Collections.singletonMap(COSRepository.TYPE,
                (metadata) -> new COSRepository(metadata, namedXContentRegistry,
                        createStorageService(metadata), clusterService, bigArrays, recoverySettings));
    }

    @Override
    public List<Setting<?>> getSettings() {
        return Arrays.asList(COSClientSettings.REGION, COSClientSettings.ACCESS_KEY_ID, COSClientSettings.ACCESS_KEY_SECRET,
                COSClientSettings.APP_ID, COSClientSettings.BUCKET,
                COSClientSettings.BASE_PATH, COSClientSettings.COMPRESS, COSClientSettings.CHUNK_SIZE, COSClientSettings.END_POINT);
    }
}