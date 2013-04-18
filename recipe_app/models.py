from django.db import models

# Create your models here.
class Hardware(models.Model):
    class Meta:
        db_table = u'hardware'
    
    name = models.CharField(max_length=50, primary_key=True)

class Ingredient(models.Model):
    class Meta:
        db_table = u'ingredient'

    name = models.CharField(max_length=100, primary_key=True)
    type = models.CharField(max_length=50, null=True, blank=True)
    dietary_value = models.CharField(max_length=50, null=True, blank=True)
    allergy = models.CharField(max_length=50, null=True, blank=True)
    vegan_vegetarian = models.CharField(max_length=20, null=True, blank=True)

class Recipe(models.Model):
    class Meta:
        db_table = u'recipe'

    id = models.IntegerField(primary_key=True)
    url = models.URLField()
    img_url = models.URLField()
    name = models.CharField(max_length=200) 
    #description = models.CharField(max_length=3000)
    #servings = models.CharField(max_length = 20)
    course = models.CharField(max_length=20, null=True, blank=True)
    method = models.CharField(max_length=20, null=True, blank=True)
    difficulty = models.CharField(max_length=20, null=True, blank=True)
    wait_time = models.IntegerField(null=True, blank=True)
    prep_time = models.IntegerField(null=True, blank=True)
    cook_time = models.IntegerField()

class RecipeHardware(models.Model):
    class Meta:
        db_table = u'recipe_hardware'

    recipe = models.ForeignKey('recipe')
    hardware = models.ForeignKey('hardware', db_column='hardware_name')

class RecipeIngredient(models.Model):
    class Meta:
        db_table = u'recipe_ingredient'

    recipe = models.ForeignKey('recipe')
    ingredient = models.ForeignKey('ingredient', db_column='ingredient_name')
    quantity = models.CharField(max_length=100)

class User(models.Model):
    class Meta:
        db_table = u'user'

    name = models.CharField(max_length=50, primary_key=True)
    pass_hash = models.CharField(max_length=100)
    allergies = models.CharField(max_length=200, null=True, blank=True)

class UserFavorite(models.Model):
    class Meta:
        db_table = u'user_favorite'

    user = models.ForeignKey('user', db_column='user_name')
    recipe = models.ForeignKey('recipe')
