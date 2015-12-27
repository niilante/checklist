package kullervo16.checklist.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kullervo16.checklist.model.*;
import java.io.File;
import java.util.List;
import kullervo16.checklist.model.dto.StepDto;

/**
 * Data object class to model a Checklist... it is backed by a YAML file.
 * 
 * @author jeve
 */
public class ChecklistImpl extends TemplateImpl implements Checklist{
      
    public ChecklistImpl(File file) {
        super(file);
    }

    public ChecklistImpl() {
    }

    public ChecklistImpl(Template template, File file) {
        super(file);
        this.description = template.getDescription();
        this.displayName = template.getDisplayName();
        this.tags = template.getTags();
        this.milestones = template.getMilestones();
        this.steps = (List<? extends StepDto>) template.getSteps();
        
    }
    
    
    
    
    
   @Override
   @JsonIgnore
   public boolean isComplete() {
       for(Step step : this.getSteps()) {
           if(!step.isComplete()) {
               // there is at least 1 not yet executed step in the list
               return false;
           }
       }
       return true;
   } 
   
   /**
    * This method calculates the worst state of the 
    * @return 
    */
   @Override
   @JsonIgnore
   public State getState() {
       State aggregatedState = State.UNKNOWN;
       for(Step step : this.getSteps()) {
           if(step.getState().compareTo(aggregatedState) > 0) {
               aggregatedState = step.getState();
           }
       }
       return aggregatedState;
   }
   
   /**
    * This method returns a percentage of progress
    * @return 
    */
   @Override
   @JsonIgnore
   public int getProgress() {
       List<? extends Step> stepWalker = this.getSteps();
       int totalSteps = stepWalker.size();
       int stepsToDo = 0;
       for(Step step : stepWalker) {
           if(!step.isComplete()) {
               stepsToDo ++;
           }
       }
       return (int)((totalSteps - stepsToDo) / (totalSteps * 0.01));
   }

}
