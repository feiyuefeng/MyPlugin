package f.test.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

/**
 * Created by Administrator on 2018/1/21/021.
 */
public class NewPlugin extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (project == null || editor == null) {
            return;
        }
        PsiFile currentEditorFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiDirectory directory = currentEditorFile.getContainingDirectory();
        PsiDirectory parentDir;
        while (true) {
            parentDir = directory.getParentDirectory();
            if ("IdeaProjects".equals(parentDir.getName())) {
                break;
            } else {
                directory = parentDir;
            }
        }
        PsiDirectory[] childDirs = parentDir.getSubdirectories();
        PsiDirectory childDir = null;
        for (PsiDirectory psiDirectory : childDirs) {
            if ("TestPlugin".equals(psiDirectory.getName())) {
                childDir = psiDirectory;
            }
        }
        if (childDir == null) {
            return;
        }
        PsiFile[] files = childDir.getFiles();
        PsiFile psiFile = null;
        for (PsiFile file : files) {
            if ("test.xml".equals(file.getName())) {
                psiFile = file;
            }
        }
        if (psiFile == null) {
            return;
        }
        XmlFile xmlFile = (XmlFile) psiFile;
        XmlTag xmlTag = xmlFile.getRootTag();
        String xmlTagName = xmlTag.getName();
        XmlDocument xmlDocument = xmlFile.getDocument();
        PsiElement[] psiElements = xmlDocument.getChildren();
        System.out.println();
        System.out.println(directory.getName());
        if (currentEditorFile == null) {
            return;
        }
        Document document = editor.getDocument();
        PsiElement element = currentEditorFile.getNavigationElement();
        String userName = askForName(project);
        sayHello(project, userName);
    }

    private String askForName(Project project) {
        return Messages.showInputDialog(project,
                "What is your name?", "Input Your Name",
                Messages.getQuestionIcon());
    }

    private void sayHello(Project project, String userName) {
        Messages.showMessageDialog(project,
                String.format("Hello, %s!\n Welcome to PubEditor.", userName), "Information",
                Messages.getInformationIcon());
    }
}