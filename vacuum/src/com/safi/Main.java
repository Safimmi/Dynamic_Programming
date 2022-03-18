package com.safi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //Read file with data input and format it --> file path by command line
        // ../Input/InputFile_01.txt
        // ../Input_Sus/input2.txt
        RoomsSet roomsSet = getInputFileData();
        assert roomsSet != null;

        //Create Adjacency Matrix: Upper Triangular Matrix (Room Order)
        int n = roomsSet.getnRoom();
        int[][] graph = new int [n][];
        int[] dirtValue_copy = roomsSet.getDirtValuesArray();

        for (int i = 0; i<n; i++){
            graph[i] = dirtValue_copy.clone();
            if(i<n-1) {dirtValue_copy[i+1] = 0; }
        }

        //Find Max Path (Based on Dijkstra) from start node to every other node.
        dijkstraMaxCost(graph, 0, roomsSet);

        //Find Max Nodes Possible.
        dijkstraMaxNodes(graph, 0, roomsSet);

        //Longest decreasing Subsequence
        longestSubsequence(roomsSet.getDirtValuesArray(), roomsSet);

    }


    public static void dijkstraMaxCost(int[][] graph, int startNode, RoomsSet roomsSet){

        int nVertex = graph.length;
        boolean[] isVisited = new boolean[nVertex];
        int[] sumCost = new int[nVertex];
        int[] previousIndex = new int[nVertex];

        //Set initial state
        for (int i = 0; i < nVertex; i++){
            isVisited[i] = false;
            sumCost[i] = -1; //The maximum path is yet not defined
            previousIndex[i] = 0;
        }

        //Start Node values
        int finalNodeIndex = 0;
        sumCost[startNode] = 0;
        previousIndex[startNode] = -1;

        for (int i = 0; i < nVertex; i++){

            int u = findMaxCost(sumCost, isVisited);
            isVisited[u] = true;

            for (int v =0 ; v < nVertex; v++){

                // Update path: total cost, final room index, previous room (path) index
                // is unvisited && is connected && new add path cost > previous one && It's not necessary (We always visit decreasingly)
                if(!isVisited[v] && graph[u][v] != 0 && (sumCost[u] + graph[u][v] > sumCost[v]) && (graph[0][u] >= graph[0][v] || u == 0)){
                    sumCost[v] = sumCost[u] + graph[u][v];
                    previousIndex[v] = u;
                }
                //Update final room
                if (sumCost[finalNodeIndex] < sumCost[v]){ finalNodeIndex = v; }

            }

        }

        // Create-Write file with solution
        printSolution(roomsSet, previousIndex, sumCost, finalNodeIndex);
        exportOutputFile("dijkstraMaxCost_result.txt", roomsSet, previousIndex, finalNodeIndex);

    }

    public static int findMaxCost(int[] sumCost, boolean[] isVisited) {

        int maxCost = -1;
        int maxCostIndex = -1;

        for (int i =0; i < sumCost.length; i++){
            if(!isVisited[i] && sumCost[i] > maxCost){
                maxCost = sumCost[i];
                maxCostIndex = i;
            }
        }

        //Exception when the list have a room that is already clean (dirtValue = 0)
        if (sumCost[0] != 0 || maxCostIndex == -1) { maxCostIndex = 0;}

        return maxCostIndex;
    }

    public static void dijkstraMaxNodes(int[][] graph, int startNode, RoomsSet roomsSet){

        int nVertex = graph.length;
        int[] sumCost = new int[nVertex];
        int[] previousIndex = new int[nVertex];

        //Set initial state
        for (int i = 0; i < nVertex; i++){
            sumCost[i] = -1; //The maximum path is yet not defined
            previousIndex[i] = 0;
        }

        //Start Node values
        int finalNodeIndex = 0;
        sumCost[startNode] = 0;
        previousIndex[startNode] = -1;

        for (int u = 0; u < nVertex; u++){

            for (int v =0 ; v < nVertex; v++){

                // Update path: total cost, final room index, previous room (path) index
                // is connected && new add path cost > previous one && has more dirt than the next room
                if(graph[u][v] != 0 && (sumCost[u] + 1 >= sumCost[v]) && (graph[0][u] >= graph[0][v] || u == 0)){
                    sumCost[v] = sumCost[u] + 1;
                    previousIndex[v] = u;
                }
                //Update final room
                if (sumCost[finalNodeIndex] < sumCost[v]){ finalNodeIndex = v; }

            }

        }

        // Create-Write file with solution
        //printSolution(roomsSet, previousIndex, sumCost, finalNodeIndex);
        exportOutputFile("dijkstraMaxNodes_result.txt", roomsSet, previousIndex, finalNodeIndex);

    }

    public static void longestSubsequence (int[] dirtValue, RoomsSet roomsSet) {

        // Longest (non-strict) decreasing subsequence
        // Worst Case
        //   Time : O(n^2)
        //   Space : O(n)

        int n = dirtValue.length;
        int[] longestSub = new int[n];
        int[] previousIndex = new int[n];
        int maxSubIndex = 0;

        Arrays.fill(longestSub, 1);

        for (int i = 1; i<n; i++){
            for (int j = 0; j < i; j++){

                if (dirtValue[i] <= dirtValue[j] && longestSub[i] < longestSub[j] + 1) {
                    longestSub[i] = longestSub[j] + 1;
                    previousIndex[i] = j;
                }

                if (longestSub[maxSubIndex] <= longestSub[i]){
                    maxSubIndex = i;
                }

            }
        }

        // Create-Write file with solution
        //printSolution(roomsSet,previousIndex,longestSub,maxSubIndex);
        exportOutputFile("subsequence_result.txt", roomsSet, previousIndex, maxSubIndex);

    }

    public static RoomsSet getInputFileData (){

        // Get file name path by command line
        Scanner scanner = new Scanner(System.in);

        try {
            String dir = scanner.nextLine();
            RoomsSet roomsSet;
            File file = new File(dir);
            Scanner inputFile = new Scanner(file);

            //Rooms
            String data = inputFile.nextLine();
            roomsSet = new RoomsSet(dir, Integer.parseInt(data) + 1);

            //Info
            roomsSet.initRoomByIndex("Start", 0, 0);

            int i = 1;
            int n = roomsSet.getnRoom();
            while (inputFile.hasNextLine() && i < n) {
                data = inputFile.nextLine();
                String[] stringData = data.split(",");
                roomsSet.initRoomByIndex(stringData[0], Integer.parseInt(stringData[1].replaceAll("\\s", "")), i);
                i++;
            }

            inputFile.close();
            return roomsSet;

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. File not found.");
            e.printStackTrace();
            return null;
        }



    }

    public static void exportOutputFile(String fileName, RoomsSet roomsSet, int[] previousIndex, int finalRoomIndex){

        try {

            //Create File
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            //Write Output
            FileWriter fileWriter = new FileWriter(fileName);
            StringBuilder outputString = new StringBuilder();
            int nRooms = 0;
            while (finalRoomIndex > 0) {
                outputString.insert(0, roomsSet.getRoomNameByIndex(finalRoomIndex) + String.format(", %s \n", roomsSet.getDirtValueByIndex(finalRoomIndex)));
                finalRoomIndex = previousIndex[finalRoomIndex];
                nRooms++;
            }
            fileWriter.write(String.format("%s \n", nRooms));
            fileWriter.write(outputString.toString());
            fileWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred. The file couldn't be created.");
            e.printStackTrace();
        }

    }

    public static void printSolution (RoomsSet roomsSet, int[] previousIndex, int[] cost, int finalIndex){

        int n = roomsSet.getnRoom();
        for (int i = 0; i<n ; i++){
            System.out.println(roomsSet.getRoomNameByIndex(i) + ", " + roomsSet.getDirtValueByIndex(i));
        }

        System.out.println("-------------------------- \n");

        //Print Solution
        System.out.println(cost[finalIndex]);
        int aux = finalIndex;
        while (aux>0){
            System.out.println( aux + ",     " + roomsSet.getRoomNameByIndex(aux) + ", " + roomsSet.getDirtValueByIndex(aux) + ", " + cost[aux] + ", " + previousIndex[aux]);
            aux = previousIndex[aux];
        }

        /*
        //Print Solution
        for (int i = 0; i < sumCost.length; i++){
            System.out.println(String.format("%s. Distance from source vertex %s to vertex %s is %s, and the previous node is %s",i , startNode, i, sumCost[i], previousIndex[i]));
        }
        System.out.println("Final Room is : " + finalNode);
        */

    }

}
