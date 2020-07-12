
import java.io.*;
import java.util.Scanner;

public class Graph {

    // andaazeye matris, deghat shavad ke matrise mojaaverat moarabaei ast va tedaade sotoon va satre baraabar darad.
    private int size;
    private   Integer[][] myArray;
    // matrise mojaaverat
    private int[][] adjacency_matrix;

    // reshteye naame file matniye voroodi
    private String input_file_path = "input_graph.csv";

    // meghdaare be andaazeye kaafi boozrg, estelaahan shebhe bi nahayat
    private int pseudo_infinity = 30000;

    // method e saazandeye class e graph
    public Graph()
    {
        Read_input_graph_csv_file();
    }



    // method e khaandane matrise daroone file
    private void Read_input_graph_csv_file()
    {

        myArray = new Integer[size][size ];
        Scanner scannerIn = null;
        int j=0;
        int colc= 0;
        int col = 0;
        String InputLine = "";
        int xnum =0;
        String filelocation;
        filelocation =" C:\\Users\\rezah\\OneDrive\\Desktop\\input_graph.csv";
        System.out.println("....");
        try{
            //setup a scanner
            scannerIn = new Scanner(new BufferedReader(new FileReader(filelocation)));
            // while ((inputline 0 scanIn.nextLine()) != null)
            while (scannerIn.hasNextLine())
            {
                // read line in from file
                InputLine = scannerIn.nextLine();
                // split the Inputline into an array at the commas
                String[] inArray = InputLine.split(",");
                size= inArray.length;

                //copy the content of the inArray to the myArray
                for (int i =0; i < inArray.length; i++)
                {
                    myArray[j][i] = Integer.parseInt(inArray[i]);
                }
                // Increment the row in the array
                j++;
            }

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    // method e peydaa kardane kootah tarin fasele beyne hameye joft gere haa
    private int[][] FloydWarshall_shortest_path()
    {
        int[][] distance_matrix = new int[size][size] ;


            /* aval az hame bayad maghadire avaliye baraye matrise masaafat moshakhas konim.
             in maghaadir ba tavajoh be vaziyate matrise hamsaayegi be dast miyayad.
            */
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (i == j)
                {
                    // ghotre asli
                    distance_matrix[i][j]  = 0;
                }
                else if (adjacency_matrix[i][j] == 0)
                {
                    // naboode yaale mostaghim
                    distance_matrix[i][j] = pseudo_infinity;
                }
                else
                {
                    // yaale mostaghim beyne i , j
                    distance_matrix[i][j] = 1;
                }
            }
        }


            /*
             halaa ma dar 3 halgheye too dar too,
            fasele haa ra ba hameye halat haye gere haye miani hesab mikonim.
            va baraye har i,j kamtarin faseleye kashf shode ra zakhire mikonim.
            */


        // k hameye haalat haye gere haaye miania ra pooshesh midahad.
        for (int k = 0; k < size; k++)
        {
            // i: yeki yeki hameye gere haa be onvaane gereye ebtedaaei
            for (int i = 0; i < size; i++)
            {
                // j: yeki yeki hameye gere haa be onvaane gereye maghsade masir
                for (int j = 0; j < size; j++)
                {
                    // agar az (i be k)+(k be j) masiri kootah tar az masire feliye i be j bashad...
                    if (distance_matrix[i][k] + distance_matrix[k][j] < distance_matrix[i][j])
                    {
                        distance_matrix[i][j]= distance_matrix[i][k] + distance_matrix[k][j];
                    }
                }
            }
        }


        return distance_matrix;
    }




    private int[][] Calculate_eccentricity_table()
    {
        // avval az hame matrise fasele ha ra hesab mikonim
        int[][]distances = FloydWarshall_shortest_path();


        // sotoone aval shomareye gere, sotoone dovom bishtarin fasele/eccentricity, sotoone sevom shomareye doortarin gere
        int[][] eccentricity_table = new int[size][3];


        for (int i = 0; i < size; i++)
        {
            // sotoone aval: shomareye khode gere
            eccentricity_table[i][0] = i;

            // sotoone dovom: meghdar dehiye avaliye be field e bishtarin fasele/eccentricity
            eccentricity_table[i][1] = distances[i][0];

            // sotoone sevom: meghdardehiye avaliye be field e doortarin gere
            eccentricity_table[i][2] = 0;


            // baraye har gere, gerei ke az an doortar ast ra ba moghayese peyda mikonim
            for (int j = 0; j < size; j++)
            {
                if (distances[i][j] > eccentricity_table[i][1])
                {
                    eccentricity_table[i][1]= distances[i][j];
                    eccentricity_table[i][2] = j;
                }
            }
        }


        return eccentricity_table;
    }




    // radius: koochaktrine meghdare 'eccentricity'
    private int Calculate_radius()
    {
        // avval az hame matrise eccetricity ra hesab va zakhire mikonim
        int[][] eccentricity_table = Calculate_eccentricity_table();


        // meghdardehiye avaliye baraye moghayese va moshakhas kardane kamtarin eccentricity
        int radius = eccentricity_table[0][1];


        // moghaseye hameye 'eccentricity' haa baraye peyda kardane koochaktarin
        for (int i = 0; i < size; i++)
        {
            if (eccentricity_table[i][1] < radius)
            {
                radius = eccentricity_table[i][1];
            }
        }


        return radius;
    }




    // diameter: bozorgtarin meghdare 'eccentricity'
    private int Calculate_diameter()
    {
        // avval az hame matrise eccetricity ra hesaab va zakhire mikonim
        int[][] eccentricity_table = Calculate_eccentricity_table();


        // meghdardehiye avaliye baraye moghayese va moshakhas kardane bishtarin eccentricity
        int diameter = eccentricity_table[0][1];


        // moghaseye hameye 'eccentricity' haa baraye peyda kardane bozorgtarin
        for (int i = 0; i < size; i++)
        {
            if (eccentricity_table[i][1] > diameter)
            {
                diameter = eccentricity_table[i][1];
            }
        }


        return diameter;
    }

    // jostojooye avale omghi
    private void DFS(Boolean[] visited, int vertex)
    {
        // bayad az shomareye 'vertex' shoroo be peymayeshe omghiy derakht konim

        visited[vertex] = true;


        // mikhahim ba in halghe tamaame yaal haye vertex ra barrasi konim
        for (int i = 0; i < size; i++)
        {
            // agar gereye vertex ba i ertebaat darad
            if (adjacency_matrix[vertex] [i]  == 1)
            {
                // va agar i az ghabl dide nashode...
                if (visited[i] == false)
                {
                    DFS(visited, i);
                }
            }
        }
    }




    public void Print_matrix()
    {
        System.out.println("------------------------Matrix anzeigen -----------------------\n\n");


        // chaape shomare haaye sotoon ha balaye matris
        System.out.println("\t  ");
        for (int c = 0; c < size; c++)
        {
            System.out.println((c + 1) + "   ");
        }

        System.out.println("\n\n\n");


        // chaape mohtavaye matris hamraah ba shomareye har radif dar ebtedaaye khat.
        // har sotoon zire shomareye marboot be khodesh ke bala chaap shod zaaher mishavad.
        for (int r = 0; r < size; r++)
        {
            System.out.println((r + 1) + "\t| ");

            for (int c = 0; c < size; c++)
            {
                System.out.println(adjacency_matrix[r][c] + " | ");
            }

            System.out.println('\n');
        }


        System.out.println("\n\n");


        System.out.println("Drücken Sie Bitte Taste, zum Hauptmenü zurückzukehren...\n");

    }




    // jadvale eccentricity ra chaap mikonad
    public void Print_eccentricity_table()
    {
        System.out.println("------------ Exzentrizitätstabelle anzeigen ------------\n\n");


        // avval az hame matrise eccetricity ra hesab va zakhire mikonim
        int[][] eccentricity_table = Calculate_eccentricity_table();


        System.out.println("Knoten\tExzentrizität\tam weitesten\n");
        for (int i = 0; i < size; i++)
        {
            System.out.println("{0}\t{1}\t\t{2}\n"+ eccentricity_table[i][0] + 1+eccentricity_table[i][1]+
                    eccentricity_table[i][2]+1);
        }

        System.out.println("\n\n");


        System.out.println("Drücken Sie Bitte eine Taste, zum Hauptmenü zurückzukehren...\n");

    }




    // matrise kootah tarin fasele haa ra chaap mikonad
    public void Print_shortest_distances()
    {
        System.out.println("------------Die Kürzesten pfad anzeigen ------------\n\n");


        // avval az hame matrise fasele ha ra hesab mikonim
        int[][] distances = FloydWarshall_shortest_path();


        // chaape shomare haaye sotoon ha balaye matris
        System.out.println("\t  ");
        for (int c = 0; c < size; c++)
        {
            System.out.println((c + 1) + "   ");
        }

        System.out.println("\n\n\n");


        // chaape mohtavaye matris hamraah ba shomareye har radif dar ebtedaaye khat.
        // har sotoon zire shomareye marboot be khodesh ke bala chaap shod zaaher mishavad.
        for (int r = 0; r < size; r++)
        {
            System.out.println((r + 1) + "\t| ");

            for (int c = 0; c < size; c++)
            {
                System.out.println(distances[r][c] + " | ");
            }

            System.out.println('\n');
        }


        System.out.println("\n\n");


        System.out.println("Drücken Sie Bitte eine Taste, zum Hauptmenü zurückzukehren...\n");

    }




    // radius koochaktarin meghdare 'eccentricity' beyne hameye maghadire gere haast.
    // chaape radius :
    public void Print_radius()
    {
        System.out.println("------------ Radius wert anzeigen ------------\n\n");

        int radius = Calculate_radius();


        System.out.println("Radius wert im Graph = {0}\n\n"+radius);


        System.out.println("Drücken Sie bitte eine Taste, zum Hauptmenü zurückzukehren...\n");

    }




    // cetral node ha, gere haaei hastand ke meghdare 'eccentricity' aan haa,
    // dar jadvale 'eccentricity' (sotoone dovom) barabar ba radius ast.
    public void Print_central_nodes()
    {
        System.out.println("------------ Zentrale Knoten anzeigen ------------\n\n");

        // tebghe tarif, central node, gerei ast ke meghdare eccentricity aan mosavi ba radius e derakht ast.
        // pas ebtedaa radius va jadvale eccentricity haa ra migirim.
        int radius = Calculate_radius();
        int[][] eccentricity_table = Calculate_eccentricity_table();


        System.out.println("Zentrale Knoten  : ");

        for (int i = 0; i < size; i++)
        {
            // agar meghdare 'eccentricity' barabar ba radius ast, in central node ast.
            if (eccentricity_table[i][1] == radius)
            {
                System.out.println("{0}, "+(i + 1));
            }
        }

        System.out.println("\n\n");



    }




    // chaape diameter :
    // radius boozorgtarin meghdare 'eccentricity' beyne hameye maghadire gere haast.
    public void Print_diameter()
    {
        System.out.println("------------ Durchmesser wert anzeigen ------------\n\n");

        int diameter = Calculate_diameter();


        System.out.println("Durchmesser wert in der Matrix = {0}\n\n"+diameter);


        System.out.println("Drücken Sie bitte eine Taste, zum Hauptmenü zurückzukehren...\n");

    }




    // mohaasebe baraye peyda kardane articulation point haa, va chaape shomareye point/gere ha
    public void Calculate_and_print_articulation_points()
    {
        System.out.println("------------ Show Articulation Points ------------\n\n");

        Boolean[] is_visited_array = new Boolean[size];
        Boolean[] is_articulation_point = new Boolean[size];
        for (int i = 0; i < size; i++)
        {
            is_visited_array[i] = false;
            is_articulation_point[i] = false;
        }


        // inja kholase saazi shod, chon graph hamband ast,
        // va baad az avalin DFS gereye dide nashode nadarim pas 0 dar nazar gerefte mishavad.


        // hala rooye hameye gere loop mizanim va aan ha ra hazf mikonim,
        // ke barrasi konim va bebinim ayaa articulation hastand ya kheyr.
        int[] temp_copy = new int[size];
        for (int i = 0; i < size; i++)
        {
            // aval: hameye ertebaatate gere ra az beyne mibarim,
            // ke amalan gere hazf shavad.
            for (int j = 0; j < size; j++)
            {
                is_visited_array[j] = false;

                temp_copy[j] = adjacency_matrix[i][j];
                adjacency_matrix[i][j] = adjacency_matrix[i][j] = 0;
            }


            // dovom: hala barasi mikonim ke ayaa graph chand teke shode ya kheyr.
            // agar ba DFS nashavad ke hameye graph ra visit kard, pas chand teke shode.
            // zamaani ke migooyim 'nashavad' hameye graph ra visit kard, yani tedade false visited bishtar az 0 shavad.


            // ejraaye DFS ba shoroo az gereye baad az hamin gere
            // albate farghri nemikonad az koja shoroo konim, faghat nabayad az hamin gere shoroo kard, chon etesaalatash ghat shode

            int start_node_for_DFS =
                    (i + 1) % size; // %size baraye etminan az mandane shomare dar bazeye 0-size ast.

            DFS(is_visited_array, start_node_for_DFS);

            // shomareshe tedaade false visited haa (dide nashode ha ke raahe dastresi nadashet and)
            int number_of_false_visited_after_DFS = 0;
            for (int j = 0; j < size; j++)
            {
                if (is_visited_array[j] == false && j != i)
                {
                    number_of_false_visited_after_DFS += 1;
                }
            }

            // aya tedade false visited haa baad az DFS bishtar az 0 shod?
            // be in mani ke ayaa gerei bood ke dastresi ba baghiye graph ra az dast dade bashad,
            // va ba yek DFS nashavad hame ar visit kard?
            // agar gerei dar asare hazfe in gereye 'i' dastresiye khod raa az dast dade, pas 'i' articulation ast.
            if (number_of_false_visited_after_DFS > 0)
            {
                is_articulation_point[i] = true;
            }


            // etesaalate gere ra be halate ghabl bar migardanim.
            for (int j = 0; j < size; j++)
            {
                adjacency_matrix[i][j] = adjacency_matrix[i][j] = temp_copy[j];
            }
        }


        // chaape liste bridge haa ba estefaade az maghadire 'is_articulation_point'
        System.out.println("Artikulations Punkte : ");
        for (int i = 0; i < size; i++)
        {
            if (is_articulation_point[i] == true)
            {
                System.out.println("{0}, "+i + 1);
            }
        }

        System.out.println("\n\n");


        System.out.println("Drücken Sie bitte eine beliebige Taste,zum Hauptmenü zurückzukehren...\n");

    }

    // mohaasebe baraye peyda kardane Bridge haa, va chaape yaal haye Bridge
    // Bridge: yaali ke agar hazf/ghat shavad, graph digar hamband nist va chand teke mishavad.
    public void Calculate_and_print_bridges()
    {
        System.out.println("------------Die Brücken zeigen ------------\n\n");


        Boolean[][] is_bridge = new Boolean[size][size ];
        for (int r = 0; r < size; r++)
        {
            for (int c = 0; c < size; c++)
            {
                is_bridge[r][c] = false;
            }
        }

        Boolean[] is_visited_array = new Boolean[size];
        for (int u = 0; u < size; u++)
        {
            for (int v = 0; v <= u; v++)
            {
                // agar dar derakhte asli beyne in gere haa yaali nist,
                // az halghe kharej sho. edaame bede va be soraaghe j badi boro.
                if (adjacency_matrix[u][v] == 0)
                {
                    continue;
                }

                // array e is_visited ra ghabl az edaame ba false por mikonim.
                for (int k = 0; k < size; k++)
                {
                    is_visited_array[k] = false;
                }
                   /* baraye taeine bridge boodane in yaal,
                       avval az hame: in yaal/etesaal ra hazf mikonim.
                       dar edame: barrasi mikonim ke aya dastresi be gereye v az tarighe baghiyeye graph momken ast?

                    */
                adjacency_matrix[u][v] = adjacency_matrix[v][u] = 0;


                // dovom: hala barasi mikonim ke ayaa graph chand teke shode ya kheyr.
                // agar ba yek DFS gereye v visit shod yani raahe digari ham hast va graph chand teke nashode.

                // ejraaye DFS ba shoroo az 'u'
                DFS(is_visited_array, u);


                // ayaa gereye 'v' bedoone yaale u->v baaz ham dide shode? (raahe digari bood?)
                if (is_visited_array[v] == false)
                {
                    is_bridge[u][v] = true;
                }


                // yaal be halate ghabl az hazf shodan barmigardad.
                adjacency_matrix[u][v] = adjacency_matrix[v][u] = 1;
            }
        }

        // chaape liste bridge haa ba estefaade az maghadire 'is_bridge'
        System.out.println("Liste der Brücken : ");
        for (int r = 0; r < size; r++)
        {
            for (int c = 0; c < size; c++)
            {
                if (is_bridge[r][c] == true)
                {
                    System.out.println("({0}->{1}), "+(r + 1)+(c + 1));
                }
            }
        }
        System.out.println("\n\n");


        System.out.println("Drücken Sie bitte eine beliebige Taste,zum Hauptmenü zurückzukehren...\n");
    }

    // chaape tedade component haa
    // dar graphe hamband ma tanha yek component darim
    public void Calculate_components()
    {
        System.out.println("dieses Produkt funktioniert nur mit dem verbündete Graph!");
        System.out.println("Es gibt nur 1 Komponente in verbundenen Graphen!\n\n");


        System.out.println("Drücken Sie bitte eine beliebige Taste,zum Hauptmenü zurückzukehren...\n");
    }
}


