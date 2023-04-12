import java.util.*;

public class VariableElimination {
    
    // observe a variable to a certain value in a factor
    public static double[][] observe(double[][] factor, int variable, int value) 
    {
        double[][] observedFactor = new double[factor.length][factor[0].length];
        for (int i = 0; i < factor.length; i++)
        {
            for (int j = 0; j < factor[0].length; j++) 
            {
                if (j == variable && i == value) 
                {
                    observedFactor[i][j] = factor[i][j];
                } else 
                {
                    observedFactor[i][j] = 0.0;
                }
            }
        }
        return observedFactor;
    }
    
    // multiply two factors
    public static double[][] multiply(double[][] factor1, double[][] factor2) 
    {
        int rows = factor1.length;
        int cols = factor1[0].length + factor2[0].length - 1;
        double[][] productFactor = new double[rows][cols];
        int index1 = 0, index2 = 0;
        for (int i = 0; i < cols; i++) 
        {
            if (i == index1 + factor1[0].length) 
            {
                index1 += factor1[0].length;
            }
            if (i == index2 + factor2[0].length - 1) 
            {
                index2 += factor2[0].length - 1;
            }
            for (int j = 0; j < rows; j++) 
            {
                if (i < index1 + factor1[0].length - 1) 
                {
                    productFactor[j][i] = factor1[j][i - index1];
                } 
                else 
                {
                    productFactor[j][i] = factor2[j][i - index2];
                }
            }
        }
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                if (j >= index1 && j < index1 + factor1[0].length - 1) 
                    productFactor[i][j] *= factor2[i][factor2[0].length - 1];
            }
        }
        return productFactor;
    }
    
    // sum out a variable in a factor
    public static double[][] sumout(double[][] factor, int variable) 
    {
        int rows = factor.length;
        int cols = factor[0].length - 1;
        double[][] resultFactor = new double[rows][cols];
        int index = 0;
        for (int i = 0; i < cols; i++) {
            if (i == variable) 
                index++;
            for (int j = 0; j < rows; j++) 
            {
                if (i == variable) 
                {
                    resultFactor[j][i] += factor[j][variable];
                } 
                else 
                {
                    resultFactor[j][i] += factor[j][index];
                }
            }
            index++;
        }
        return resultFactor;
    }
    
    // normalize a factor
    public static double[][] normalize(double[][] factor) 
    {
        double[][] normalizedFactor = new double[factor.length][factor[0].length];
        double sum = 0.0;
        for (int i = 0; i < factor.length; i++) 
        {
            for (int j = 0; j < factor[0].length; j++) 
            {
                sum += factor[i][j];
            }
        }
        for (int i=0; i<factor.length; i++)
        {
            for(int j=0; j<factor[0].length; j++)
            {
                normalizedFactor[i][j]=factor[i][j]/sum;
            }
        }
        return normalizedFactor;
    }

    //computing Pr(queryVariables | evidenceList) by variable Elimination
    public static double [][] inference(double [][][] factorList, int[] queryVariables, int[] orderedListOfHiddenVariables, int[] evidenceList)
    {
        //restrict factors according to evidence
        for(int i=0; i<evidenceList.length; i++)
        {
            if(evidenceList[i]!=-1)
            {
                for(int i=0; i<orderedListOfHiddenVariables.length; i++)
                {
                    List<Integer> factorsContainingVariable=new ArrayList<Integer>();
                    for(int j=0; j<factorList.length; j++)
                    {
                        if(Arrays.asList(j).contains(orderedListOfHiddenVariables[i]))
                            factorsContainingVariable.add(j);
                    }
                }
            }
        }

        //eliminating hidden variables
        for(int i=0; i<orderedListOfHiddenVariables.length; i++)
        {
            List<Integer> factorsConatainingVariable = new ArrayList<Integer>();
            for(int j=0; j<factorList.length; j++)
            {
                if(Arrays.asList(j).contains(orderedListOfHiddenVariables[i]))
                    factorsContainingVariable.add(j);
            }

            if(factorsContainingVariable.size()>0)
            {
                double[][] productFactor= factorsList[factorsContainingVariable.get(0)];
                for(int j=1; j<factosContainingVariable.size();j++)
                {
                    productFactor= multiply(productFactor, factorList[factorsContainingVariable.get(j)];)
                }
                productFactor= sumout(productFactor, orderedListOfHiddenVariables[i]);
                for(int j =0; j<factorList.length; j++)
                {
                    if(!factorContainingVariable.contains(j))
                        factorList[j]= multiply(factorList[j], productFactor);
                }
                factorList[factorsContainingVariable.get(0)]= productFactor;
            }
        }

        //multiplying remaining factors
        double[][] resultFactor=factorList[0];
        for(int i=1; i<factorList.length; i++)
        {
            resultFactor= multiply(resultFactor, factorList[i]);
        }

        //normalizing the resultFactor
        resultFactor= normalize(resultFactor);

        return resultFactor;

    }


}