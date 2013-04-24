import networkFoodCrawler
import allrecipiesCrawler

a_recipe_id = 1
for s in range(0, 100):
    a_recipe_id = networkFoodCrawler.netCrawler(recipe_id = a_recipe_id, n = s)
    
    a_recipe_id = allrecipiesCrawler.allCrawler(recipe_id = a_recipe_id, n = s)

    print a_recipe_id
print 'yay! we did it : D'
