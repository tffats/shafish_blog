``` java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcurrentDiskUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentDiskUtil.class);
    private static final String READ_ONLY = "r";
    private static final String READ_WRITE = "rw";
    private static final int RETRY_COUNT = 10;
    private static final int SLEEP_BASETIME = 10;

    public ConcurrentDiskUtil() {
    }

    public static String getFileContent(String path, String charsetName) throws IOException {
        File file = new File(path);
        return getFileContent(file, charsetName);
    }

    public static String getFileContent(File file, String charsetName) throws IOException {
        RandomAccessFile fis = new RandomAccessFile(file, "r");
        Throwable var3 = null;

        String var10;
        try {
            FileChannel fcin = fis.getChannel();
            Throwable var5 = null;

            try {
                FileLock rlock = tryLock(file, fcin, true);
                Throwable var7 = null;

                try {
                    int fileSize = (int)fcin.size();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);
                    fcin.read(byteBuffer);
                    byteBuffer.flip();
                    var10 = byteBufferToString(byteBuffer, charsetName);
                } catch (Throwable var54) {
                    var7 = var54;
                    throw var54;
                } finally {
                    if (rlock != null) {
                        if (var7 != null) {
                            try {
                                rlock.close();
                            } catch (Throwable var53) {
                                var7.addSuppressed(var53);
                            }
                        } else {
                            rlock.close();
                        }
                    }

                }
            } catch (Throwable var56) {
                var5 = var56;
                throw var56;
            } finally {
                if (fcin != null) {
                    if (var5 != null) {
                        try {
                            fcin.close();
                        } catch (Throwable var52) {
                            var5.addSuppressed(var52);
                        }
                    } else {
                        fcin.close();
                    }
                }

            }
        } catch (Throwable var58) {
            var3 = var58;
            throw var58;
        } finally {
            if (fis != null) {
                if (var3 != null) {
                    try {
                        fis.close();
                    } catch (Throwable var51) {
                        var3.addSuppressed(var51);
                    }
                } else {
                    fis.close();
                }
            }

        }

        return var10;
    }

    public static Boolean writeFileContent(String path, String content, String charsetName) throws IOException {
        File file = new File(path);
        return writeFileContent(file, content, charsetName);
    }

    public static Boolean writeFileContent(File file, String content, String charsetName) throws IOException {
        if (!file.exists() && !file.createNewFile()) {
            return false;
        } else {
            try {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                Throwable var4 = null;

                try {
                    FileChannel channel = raf.getChannel();
                    Throwable var6 = null;

                    try {
                        FileLock lock = tryLock(file, channel, false);
                        Throwable var8 = null;

                        try {
                            byte[] contentBytes = content.getBytes(charsetName);
                            ByteBuffer sendBuffer = ByteBuffer.wrap(contentBytes);

                            while(sendBuffer.hasRemaining()) {
                                channel.write(sendBuffer);
                            }

                            channel.truncate((long)contentBytes.length);
                            return true;
                        } catch (Throwable var56) {
                            var8 = var56;
                            throw var56;
                        } finally {
                            if (lock != null) {
                                if (var8 != null) {
                                    try {
                                        lock.close();
                                    } catch (Throwable var55) {
                                        var8.addSuppressed(var55);
                                    }
                                } else {
                                    lock.close();
                                }
                            }

                        }
                    } catch (Throwable var58) {
                        var6 = var58;
                        throw var58;
                    } finally {
                        if (channel != null) {
                            if (var6 != null) {
                                try {
                                    channel.close();
                                } catch (Throwable var54) {
                                    var6.addSuppressed(var54);
                                }
                            } else {
                                channel.close();
                            }
                        }

                    }
                } catch (Throwable var60) {
                    var4 = var60;
                    throw var60;
                } finally {
                    if (raf != null) {
                        if (var4 != null) {
                            try {
                                raf.close();
                            } catch (Throwable var53) {
                                var4.addSuppressed(var53);
                            }
                        } else {
                            raf.close();
                        }
                    }

                }
            } catch (FileNotFoundException var62) {
                throw new IOException("file not exist");
            }
        }
    }

    public static String byteBufferToString(ByteBuffer buffer, String charsetName) throws IOException {
        Charset charset = Charset.forName(charsetName);
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
        return charBuffer.toString();
    }

    private static void sleep(int time) {
        try {
            Thread.sleep((long)time);
        } catch (InterruptedException var2) {
            InterruptedException e = var2;
            LOGGER.warn("sleep wrong", e);
            Thread.currentThread().interrupt();
        }

    }

    private static FileLock tryLock(File file, FileChannel channel, boolean shared) throws IOException {
        FileLock result = null;
        int i = 0;

        do {
            try {
                result = channel.tryLock(0L, Long.MAX_VALUE, shared);
            } catch (Exception var6) {
                Exception e = var6;
                ++i;
                if (i > 10) {
                    LOGGER.error("[NA] lock " + file.getName() + " fail;retryed time: " + i, e);
                    throw new IOException("lock " + file.getAbsolutePath() + " conflict");
                }

                sleep(10 * i);
                LOGGER.warn("lock " + file.getName() + " conflict;retry time: " + i);
            }
        } while(null == result);

        return result;
    }
}
```