package de.trafficsimulation;

/* 
 Usage of special characters: Use the hexadecimal coding in the third column
 of the following list preceeded by a backslash and "u00" 
 Example: Ã¸=\u00F8
 So, if you want to say "SmÃ¸rebrÃ¸d", write it as "sm\u00F8rebr\u00F8d"

 Oct   Dec   Hex   Char   Description

 300   192   C0     Ã€     LATIN CAPITAL LETTER A WITH GRAVE
 301   193   C1     Ã�     LATIN CAPITAL LETTER A WITH ACUTE
 302   194   C2     Ã‚     LATIN CAPITAL LETTER A WITH CIRCUMFLEX
 303   195   C3     Ãƒ     LATIN CAPITAL LETTER A WITH TILDE
 304   196   C4     Ã„     LATIN CAPITAL LETTER A WITH DIAERESIS
 305   197   C5     Ã…     LATIN CAPITAL LETTER A WITH RING ABOVE
 306   198   C6     Ã†     LATIN CAPITAL LETTER AE
 307   199   C7     Ã‡     LATIN CAPITAL LETTER C WITH CEDILLA
 310   200   C8     Ãˆ     LATIN CAPITAL LETTER E WITH GRAVE
 311   201   C9     Ã‰     LATIN CAPITAL LETTER E WITH ACUTE
 312   202   CA     ÃŠ     LATIN CAPITAL LETTER E WITH CIRCUMFLEX
 313   203   CB     Ã‹     LATIN CAPITAL LETTER E WITH DIAERESIS
 314   204   CC     ÃŒ     LATIN CAPITAL LETTER I WITH GRAVE
 315   205   CD     Ã�     LATIN CAPITAL LETTER I WITH ACUTE
 316   206   CE     ÃŽ     LATIN CAPITAL LETTER I WITH CIRCUMFLEX
 317   207   CF     Ã�     LATIN CAPITAL LETTER I WITH DIAERESIS
 320   208   D0     Ã�     LATIN CAPITAL LETTER ETH
 321   209   D1     Ã‘     LATIN CAPITAL LETTER N WITH TILDE
 322   210   D2     Ã’     LATIN CAPITAL LETTER O WITH GRAVE
 323   211   D3     Ã“     LATIN CAPITAL LETTER O WITH ACUTE
 324   212   D4     Ã”     LATIN CAPITAL LETTER O WITH CIRCUMFLEX
 325   213   D5     Ã•     LATIN CAPITAL LETTER O WITH TILDE
 326   214   D6     Ã–     LATIN CAPITAL LETTER O WITH DIAERESIS
 327   215   D7     Ã—     MULTIPLICATION SIGN
 330   216   D8     Ã˜     LATIN CAPITAL LETTER O WITH STROKE
 331   217   D9     Ã™     LATIN CAPITAL LETTER U WITH GRAVE
 332   218   DA     Ãš     LATIN CAPITAL LETTER U WITH ACUTE
 333   219   DB     Ã›     LATIN CAPITAL LETTER U WITH CIRCUMFLEX
 334   220   DC     Ãœ     LATIN CAPITAL LETTER U WITH DIAERESIS
 335   221   DD     Ã�     LATIN CAPITAL LETTER Y WITH ACUTE
 336   222   DE     Ãž     LATIN CAPITAL LETTER THORN
 337   223   DF     ÃŸ     LATIN SMALL LETTER SHARP S
 340   224   E0     Ã      LATIN SMALL LETTER A WITH GRAVE
 341   225   E1     Ã¡     LATIN SMALL LETTER A WITH ACUTE
 342   226   E2     Ã¢     LATIN SMALL LETTER A WITH CIRCUMFLEX
 343   227   E3     Ã£     LATIN SMALL LETTER A WITH TILDE
 344   228   E4     Ã¤     LATIN SMALL LETTER A WITH DIAERESIS
 345   229   E5     Ã¥     LATIN SMALL LETTER A WITH RING ABOVE
 346   230   E6     Ã¦     LATIN SMALL LETTER AE
 347   231   E7     Ã§     LATIN SMALL LETTER C WITH CEDILLA
 350   232   E8     Ã¨     LATIN SMALL LETTER E WITH GRAVE
 351   233   E9     Ã©     LATIN SMALL LETTER E WITH ACUTE
 352   234   EA     Ãª     LATIN SMALL LETTER E WITH CIRCUMFLEX
 353   235   EB     Ã«     LATIN SMALL LETTER E WITH DIAERESIS
 354   236   EC     Ã¬     LATIN SMALL LETTER I WITH GRAVE
 355   237   ED     Ã­     LATIN SMALL LETTER I WITH ACUTE
 356   238   EE     Ã®     LATIN SMALL LETTER I WITH CIRCUMFLEX
 357   239   EF     Ã¯     LATIN SMALL LETTER I WITH DIAERESIS
 360   240   F0     Ã°     LATIN SMALL LETTER ETH
 361   241   F1     Ã±     LATIN SMALL LETTER N WITH TILDE
 362   242   F2     Ã²     LATIN SMALL LETTER O WITH GRAVE
 363   243   F3     Ã³     LATIN SMALL LETTER O WITH ACUTE

 364   244   F4     Ã´     LATIN SMALL LETTER O WITH CIRCUMFLEX
 365   245   F5     Ãµ     LATIN SMALL LETTER O WITH TILDE
 366   246   F6     Ã¶     LATIN SMALL LETTER O WITH DIAERESIS
 367   247   F7     Ã·     DIVISION SIGN
 370   248   F8     Ã¸     LATIN SMALL LETTER O WITH STROKE
 371   249   F9     Ã¹     LATIN SMALL LETTER U WITH GRAVE
 372   250   FA     Ãº     LATIN SMALL LETTER U WITH ACUTE
 373   251   FB     Ã»     LATIN SMALL LETTER U WITH CIRCUMFLEX
 374   252   FC     Ã¼     LATIN SMALL LETTER U WITH DIAERESIS
 375   253   FD     Ã½     LATIN SMALL LETTER Y WITH ACUTE
 376   254   FE     Ã¾     LATIN SMALL LETTER THORN
 377   255   FF     Ã¿     LATIN SMALL LETTER Y WITH DIAERESIS

 */

public class Language
{

    private static Language instance;

    // support for following languages:
    // German:   index=0
    // Englisch: index=1
    // Brasil: index=2
    // French: index=3
    // Turkish: index=4
    // Catalan: index=5
    // Italiano: index=6
    // Romanian: index=7

    private Language()
    {
        // empty 
    }

    public static Language getInstance()
    {
        if (instance == null)
        {
            instance = new Language();
        }

        return instance;
    }

    private int langIndex = Constants.DEFAULT_LANG_INDEX;

    public void setIndex(int i)
    {
        if (i < 0 || i > CAR_STRING.length)
        {
            // error ...
            System.err.println("parameter: language index " + i + " currently not supported ...");
        }
        else
        {
            langIndex = i;
        }
    }

    public int index()
    {
        return langIndex;
    }

    // in SimCanvas:

    private String[] CAR_STRING = {"PKW", "Car", "Carro", "Voiture", "Otomobil", "Cotxe", "Autovettura", "Automobil"};

    public String getCarString()
    {
        return CAR_STRING[langIndex];
    }

    private String[] TRUCK_STRING = {"LKW", "Truck", "Caminh\u00E3o", "Camions", "Kamyon", "Cami\u00F3", "Autocarro",
            "Camion"};

    public String getTruckString()
    {
        return TRUCK_STRING[langIndex];
    }

    private String[] ID_STRING = {"Gleicher PKW-Typ", "Same Car", "Mesmo Carro", "M\u00EAme voiture",
            "Ayn\u00FD Ara\u00E7", "Mateix cotxe", "Stessa Autovettura", "Aceeasi masina"};

    public String getIdString()
    {
        return ID_STRING[langIndex];
    }

    private String[] PROBE_VEH_STRING = {"Testfahrzeug", "Probe Car", "Carro de Teste", "Voiture test",
            "Test Arac\u00FD", "Cotxe de prova", "Autovettura di Prova", "Masina de test"};

    public String getProbeVehString()
    {
        return PROBE_VEH_STRING[langIndex];
    }

    private String[] SIM_TIME = {"Simulierte Zeit  ", "Time  ", "Tempo  ", "Temps ", "Zaman", "Temps", "Tempo",
            "Timp simulare"};

    public String getSimTime()
    {
        return SIM_TIME[langIndex];
    }

    private String[] UPHILL_MARKER = {"Steigung", "uphill", "ladeira", "inclinaison", "Yoku\u00FE Yukar\u00FD",
            "Pendent", "Pendenza", "Panta"};

    public String getUmhillMarker()
    {
        return UPHILL_MARKER[langIndex];
    }

    private String[] UPHILL_MARKER_BEGIN = {"Beginn", "Begin", "Come\u00E7o", "D\u00E9but", "Ba\u00FElang\u00FD\u00E7",
            "Pendent initio", "Inizio Salita", "Start"};

    public String getUphillMarkerBegin()
    {
        return UPHILL_MARKER_BEGIN[langIndex];
    }

    // in MicroSim:

    private String[] DESIRED_VEL = {"Wunschgeschwindigkeit v0", "Desired Velocity v0", "Velocidade Desejada v0",
            "Vitesse souhait\u00E9e v0", "\u00DDzin Verilen H\u00FDz v0", "max Velocitat v0",
            "Velocit\u00e0 Desiderata v0", "Viteza dorita"};

    public String getDesiredVelIDM()
    {
        return DESIRED_VEL[langIndex];
    }

    private String[] DESIRED_HEADWAY = {"Zeitl\u00FCcke T", "Time gap T", "Intervalo de tempo T",
            "Intervalle de temps T", "Zaman Aral\u00FD\u00F0\u00FD T", "Time gap T", "Intervallo di Tempo T",
            "Interval de timp"};

    public String getDesiredHeadwayIDM()
    {
        return DESIRED_HEADWAY[langIndex];
    }

    private String[] DESIRED_ACC = {"Beschleunigung a", "Acceleration a", "Acelera\u00E7\u00E3o a",
            "Acc\u00E9l\u00E9ration a", "Acceleration a", "Acceleration a", "Accelerazione a", "Acceleratie"};

    public String getDesiredAccelIDM()
    {
        return DESIRED_ACC[langIndex];
    }

    private String[] DESIRED_DECEL = {"Bremsverz\u00F6gerung b", "Deceleration b", "Desacelera\u00E7\u00E3o b",
            "D\u00E9c\u00E9l\u00E9ration b", "Yava\u00FElama \u00DDvmesi b", "Deceleration b", "Decelerazione b",
            "Deceleratie"};

    public String getDesiredDecelIDM()
    {
        return DESIRED_DECEL[langIndex];
    }

    private String[] MIN_GAP = {"Abstand s0 im Stau", "Minimum gap s0", "Dist\u00E2ncia m\u00EDnima s0",
            "\u00C9cart minimal s0", "Minimum gap s0", "Minimum gap s0", "Distanza Minima s0", "Distanta minima"};

    public String getMinGapIDM()
    {
        return MIN_GAP[langIndex];
    }

    private String[] IDM_S1 = {"Abstand s1", "Distance s1", "Dist\u00E2ncia s1", "Distance s1", "Takip Mesafesi",
            "Distance s1", "Distanza s1", "Distanta s1"};

    public String getS1IDM()
    {
        return IDM_S1[langIndex];
    }

    private String[] AVG_DENSITY = {"Verkehrsdichte", "Average Density", "Densidade M\u00E9dia",
            "Densit\u00E9 moyenne", "Yo\u00F0unluk", "Densitat mitjana", "Quantit\u00e0 di Traffico",
            "Densitate medie trafic"};

    public String getAvgDensity()
    {
        return AVG_DENSITY[langIndex];
    }

    private String[] INFLOW = {"Haupt-Zufluss", "Main Inflow", "Fluxo de entrada Principal", "Flux entrant principal",
            "Anayol Ak\u00FD\u00FE Miktar\u00FD", "Flux principal", "Flusso principale", "Flux principal intrare"};

    public String getInflow()
    {
        return INFLOW[langIndex];
    }

    private String[] RAMP_INFLOW = {"Zufluss der Zufahrt", "Ramp Inflow", "Fluxo de entrada Rampa",
            "Demande de la bretelle d'acc\u00E8s", "Kat\u00FDl\u00FDm Ak\u00FD\u00FE Miktar\u00FD",
            "Flux incorporaci\u00F3", "Flusso in Entrata", "Flux rampa"};

    public String getRampInflow()
    {
        return RAMP_INFLOW[langIndex];
    }

    private String[] MOBIL_POLITE = {"H\u00F6flichkeitsfaktor", "Politeness Factor", "Fator de Polidez",
            "Degr\u00E9 de politesse", "Yol Verme", "Factor de cortesia", "Fattore di Cortesia", "Factor de politete"};

    public String getMobilPoliteness()
    {
        return MOBIL_POLITE[langIndex];
    }

    private String[] MOBIL_RAMP_POLIT = {"p-Faktor Zufahrt", "p-factor ramp", "Fator-p Rampa",
            "Coefficient p de la bretelle d'ac\u00E8s", "Kat\u00FDl\u00FDm Yol Verme",
            "Factor de cortesia a la incorporaci\u00F3", "Fattore di Cortesia in Rampa", "Factor rampa"};

    public String getMobilPolitenessRamp()
    {
        return MOBIL_RAMP_POLIT[langIndex];
    }

    private String[] TRUCK_PERC = {"LKW-Anteil", "Truck Percentage", "Porcentagem de Caminh\u00F5es",
            "Proportion de camions", "Kamyon Miktar\u00FD", "Percentatge de camions", "Percentuale di Autocarri",
            "Procentaj camioane"};

    public String getTruckPerc()
    {
        return TRUCK_STRING[langIndex];
    }

    private String[] MOBIL_THRES = {"Wechselschwelle", "Changing Threshold", "Limite de Altera\u00E7\u00E3o",
            "Limitation de changement de voie", "\u00FEerit De\u00F0i\u00FEtirme", "Changing Threshold",
            "Limite di Cambiamento", "Schimbare prag"};

    public String getMobilThreshold()
    {
        return MOBIL_THRES[langIndex];
    }

    private String[] MOBIL_RAMP_BIAS = {"a_bias,Zufahrt", "a_bias,onramp", "a_bias,rampa", "a_bias, acc\u00E8s",
            "Kat\u00FDl\u00FDm", "Biaix d'acceleraci\u00F3", "a_bias in Rampa", "Accelerare rampa"};

    public String getMobilRampBias()
    {
        return MOBIL_RAMP_BIAS[langIndex];
    }

    private String[] TIME_WARP = {"Zeitlicher Warp-Faktor", "Time Warp Factor", "Fator de Time Warp",
            "Dilatation temporelle", "Zaman Katsay\u00FDs\u00FD", "Factor de temps", "Rateo di Simulazione",
            "Factor de timp"};

    public String getTimeWarp()
    {
        return TIME_WARP[langIndex];
    }

    private String[] FRAMERATE = {" - fach", " times", " vezes", " fois", "Kat\u00FD", "vegades", "Volte", "Cadre"};

    public String getFramerate()
    {
        return FRAMERATE[langIndex];
    }

    private String[] SPEEDLIMIT = {"Tempolimit", "Imposed Speed Limit", "Limite de Velocidade",
            "Limitation de vitesse", "H\u00FDz S\u00FDn\u00FDr\u00FD", "Velocitat l\u00EDmit imposada",
            "Limite di Velocit\u00e0", "Limita de viteza"};

    public String getSpeedlimit()
    {
        return SPEEDLIMIT[langIndex];
    }

    private String[] SPEED = {"Geschwindigkeit", "Speed", "Velocidade", "Vitesse", "S\u00FDn\u00FDr\u00FD",
            "Velocitat", "Velocit\u00e0", "Viteza"};

    public String getSpeed()
    {
        return SPEED[langIndex];
    }

    private String[] VEH_PER_HOUR = {" Kfz/h", " Vehicles/h", " Ve\u00EDculos/h", "V\u00E9hicules/h", " Vehicles/h",
            "Vehicles/h", "Veicoli/h", "Autovehicule/h"};

    public String getVehPerHour()
    {
        return VEH_PER_HOUR[langIndex];
    }

    private String[] VEH_PER_KM = {" Kfz/km/Spur", " Veh./km/lane", " Ve\u00EDc./km/pista", "V\u00E9h./km/voie",
            "Ara\u00E7/km/\u00FEerit", "Vehicles/km/carril", "Veicoli/h/per Corsia", "Autovehicule/km/banda"};

    public String getVehPerKm()
    {
        return VEH_PER_KM[langIndex];
    }

    private String[] START = {"Start", "Start", "Iniciar", "D\u00E9marrer", "Start", "Iniziare", "Start"};

    public String getStartName()
    {
        return START[langIndex];
    }

    private String[] STOP = {"Stop", "Stop", "Pausa", "Arr\u00EAter", "Dur", "Stop", "Pausa", "Stop"};

    public String getStopName()
    {
        return STOP[langIndex];
    }

    private String[] SCEN_RING = {"Ringstra\u00DFe", "Ring Road", "Estrada Circular", "Circuit", "D\u00F6nel",
            "Anella", "Circuito ad Anello", "Circuit"};

    public String getScenRingName()
    {
        return SCEN_RING[langIndex];
    }

    private String[] SCEN_ONRAMP = {"Zufahrt", "On-Ramp", "Com Rampa", "Bretelle d'acc\u00E8s", "Kat\u00FDl\u00FDm",
            "Incorporaci\u00F3", "Rampa di Ingresso", "Scenariu intrare secundara"};

    public String getScenOnrampName()
    {
        return SCEN_ONRAMP[langIndex];
    }

    private String[] SCEN_LANE_CLOSING = {"Spursperrung", "Laneclosing", "Pista fechada", "Voie ferm\u00E9",
            "\u00FEerit Kapanmas\u00FD", "Carril tancat", "Corsia Chiusa", "Apropiere obstacol"};

    public String getScenLaneclosingName()
    {
        return SCEN_LANE_CLOSING[langIndex];
    }

    private String[] SCEN_UPHILL = {"Steigung", "Uphill Grade", "Com Ladeira", "Route en pente", "Rampa", "Pendent",
            "Strada in Salita", "Rampa"};

    public String getScenUphillName()
    {
        return SCEN_UPHILL[langIndex];
    }

    private String[] SCEN_TRAFFIC_LIGHTS = {"Stadtverkehr", "Traffic Lights", "Sem\u00E1foro", "Feux tricolores",
            "Trafik I\u00FE\u00FDklar\u00FD", "Sem\u00E0for", "Semaforo", "Semafor"};

    public String getScenTrafficlightsName()
    {
        return SCEN_TRAFFIC_LIGHTS[langIndex];
    }

    private String[] BUTTON_PERTURBATION = {"Verursache St\u00F6rung!", "Apply Perturbation!",
            "Causar Perturba\u00E7\u00E3o!", "Appliquer une perturbation", "Sorun Yarat", "Apply Perturbation!",
            "Inserisci Perturbazione", "Perturbatie"};

    public String getPerturbationButton()
    {
        return BUTTON_PERTURBATION[langIndex];
    }

}
