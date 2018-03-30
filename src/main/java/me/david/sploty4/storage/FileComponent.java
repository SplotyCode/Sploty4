package me.david.sploty4.storage;

import java.io.IOException;

public interface FileComponent {

    void read(FileSerializer serializer) throws IOException;
    void write(FileSerializer serializer) throws IOException;
}
