package kullervo16.checklist.service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import kullervo16.checklist.model.TemplateInfo;
import kullervo16.checklist.model.dto.TemplateDto;
import kullervo16.checklist.model.impl.TemplateImpl;



/**
 * Repository that parses the directory template structure.
 * 
 * @author jeve
 */
@Singleton
public class TemplateRepository {

    private Map<String,TemplateImpl> data = new HashMap<>();
    
    @PostConstruct
    public void init() {
        // TODO : fetch from config
        this.loadData("/home/jef/NetBeansProjects/checklist/src/test/resources/data/templates");
    }
        
    /**
     * This method scans the directory structure and determines which templates are available.
     * @param folder the directory to scan
     */
    
    public synchronized void loadData(String folder) {
        Map<String,TemplateImpl> newData = new HashMap<>();
        this.scanDirectoryForTemplates(new File(folder), "", newData);
        this.data = newData;
    }
    
    private void scanDirectoryForTemplates(File startDir, String prefix,Map<String,TemplateImpl> newModel) {
        for(File f : startDir.listFiles()) {
            if(f.isDirectory()) {
                this.scanDirectoryForTemplates(f, prefix+"/"+f.getName(), newModel);
            } else {
                newModel.put(prefix+"/"+f.getName().substring(0,f.getName().lastIndexOf(".")), new TemplateImpl(f)); 
            }
            
        }
    }

    public List<String> getTemplateNames() {
        LinkedList<String> names = new LinkedList<>(data.keySet());        
        return names;
                
    }

    public TemplateDto getTemplate(String templateName) {
        return this.data.get(templateName);
    }

    public synchronized List<TemplateInfo> getTemplateInformation() {
        List<TemplateInfo> result = new LinkedList<>();
        for(Entry<String,TemplateImpl> entry : this.data.entrySet()) {
            TemplateInfo ti = new TemplateInfo();
            ti.setId(entry.getKey());
            
            ti.setDescription(entry.getValue().getDescription());    
            ti.setMilestones(entry.getValue().getMilestones());
            ti.setTags(entry.getValue().getTags());
            result.add(ti);
        }
        Collections.sort(result);
        return result;
    }

    
}
