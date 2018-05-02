package me.david.sploty4.storage;

import java.io.IOException;

public interface FileComponent {

    void read(final FileSerializer serializer) throws IOException;
    void write(final FileSerializer serializer) throws IOException;
}
