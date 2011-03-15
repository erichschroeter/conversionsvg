package conversion.ui;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

abstract class AbstractFileSystemTestCase extends TestCase {
    public static final File DEFAULT_TEST_ROOT =
            new File("tmp").getAbsoluteFile();
    private File testRoot;

    protected final void setUp() throws Exception {
        super.setUp();

        String testRoot = System.getProperty("net.sf.fstreem.testRoot");
        if (null == testRoot || "".equals(testRoot)) {
            this.testRoot = DEFAULT_TEST_ROOT;
        } else {
            this.testRoot = new File(testRoot).getAbsoluteFile();
        }

        cleanupTestRoot();
        assertTrue("unable to create testRoot: [" + this.testRoot + "]",
                   this.testRoot.mkdirs());

        childSetUp();
    }

    protected void childSetUp() throws IOException {
    }

    protected final File getTestRoot() {
        return testRoot;
    }

    protected File createFile(String pathname) throws IOException {
        File file = new File(getTestRoot(), pathname);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } finally {
            if (null != fos) {
                fos.close();
            }
        }

        assertTrue("created file does not exist: [" + file + "]",
                   file.exists());
        return file;
    }

    protected File createFolder(String pathname) {
        File folder = new File(getTestRoot(), pathname);
        assertTrue("could not create folder: [" + folder + "]",
                   folder.mkdirs());
        return folder;
    }

    protected void cleanupTestRoot() {
        if (getTestRoot().exists()) {
            cleanup(getTestRoot());
        }
    }

    protected void cleanup(File root) {
        if (null == root) {
            return;
        }

        File[] children = root.listFiles();
        if (null != children) {
            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                cleanup(child);
            }
        }

        assertTrue("unable to delete [" + root + "]",
                   root.delete());
    }
}
