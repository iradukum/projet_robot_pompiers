package strategie;

import donneesSimulation.DonneesSimulation;
import scenario.Simulateur;

/*********************************************************************************************
 * Classe ChefPompier permettant de définir des stratégies d'affectation des tâches, organiser 
 * les déplacements et interventions des robots pour éteindre les incendies au plus vite
 *********************************************************************************************/

public abstract class ChefPompier {
    protected DonneesSimulation data; 
    protected Simulateur simu;

    public ChefPompier(Simulateur simu){
        this.simu = simu; 
        this.data = simu.getData(); 
    }

    public abstract void assignTasks();
}
