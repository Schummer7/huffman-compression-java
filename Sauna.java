
import es.upm.babel.cclib.Semaphore;
public class Sauna {
	  // Dos intensidades:
    static final int ALTA  = 0;
    static final int MEDIA = 1;
    // La capacidad es 3, de momento
    static final int CAPAC = 3;
    
    // TODO: declaración e inicialización de los semáforos
    // necesarios
    // 
   Semaphore cerrojo= new Semaphore(1);
   Semaphore turnoA=new Semaphore(0);
   Semaphore turnoM=new Semaphore(0);
     
    volatile static int sa=2;// empieza sin tipo
    volatile static int personas=0;
    volatile static int AL=0;
    volatile static int ME=0;
    public Sauna() {
    }
    
    public void entrar(int tipo) {
    	cerrojo.await();
        switch (tipo) {
     
	case ALTA:
		
	   if((personas>0 && sa==MEDIA) || personas==CAPAC) {
		   AL++;
		   cerrojo.signal();
		   turnoA.await();
		   
	   }
	   else {
		   sa=ALTA;
		   personas++;
		   cerrojo.signal();
		  
		  }
	   
	    
	    break;
	case MEDIA:
		if((personas>0 && sa==ALTA) || personas==CAPAC) {
			   ME++;
			   cerrojo.signal();
			   turnoM.await();
			   
		   }
		   else {
			   sa=MEDIA;
			   personas++;
			   cerrojo.signal();
			  
			  }
		
	    break;
	}
    }

    public void salir(int tipo) {
    	cerrojo.await();
    	personas--;
	// TODO: protocolo de salida de la sauna. Debe anotarse que
	// sale alguien y quizá desbloquear a proceso(s) en espera.
	switch (tipo) {
	case ALTA:
		if(personas==0) {
			if(ME>0) {
				sa=MEDIA;
				while(personas<CAPAC && ME>0) {
					personas++;
					ME--;
					turnoM.signal();
				}
			}
			else {
				sa=2;
			}
		}
		else if(AL>0 && personas<CAPAC) {
			personas++;
			AL--;
			turnoA.signal();
		}
	    break;
	case MEDIA:
		if(personas==0) {
			if(AL>0) {
				sa=ALTA;
				while(personas <CAPAC && AL>0) {
					personas++;
					AL--;
					turnoA.signal();
				}
			}
			else {
				sa=2;
			}
		}
		else if(ME>0 && personas<CAPAC) {
			personas++;
			ME--;
			turnoM.signal();
		}
	    break;
	}
	cerrojo.signal();
    }
}
