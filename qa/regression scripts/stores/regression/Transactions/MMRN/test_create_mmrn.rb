require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_item_methods.rb")
require path_to_file("stores_mmrn_methods.rb")

include Stores_common_methods
include Stores_item_methods
include Stores_mmrn_methods



class Stores_TC_Create_MMRN< Test::Unit::TestCase 
  
def setup

@pid = Process.create(
:app_name => "ruby \"#{path_to_file("clicker.rb")}\"",
:creation_flags => Process::DETACHED_PROCESS
).process_id

login_to_stores

#~ rescue Exception => e
#~ puts e

end


def test_0010_create_mmrn_for_physical_stocking

      tc = '0010'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)

end

def test_0020_create_mmrn_for_Store_Return_Note
  
      tc = '0020'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      
end

def test_0030_create_mmrn_for_scrap

      tc = '0030'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)

end
  
def test_0040_create_mmrn_for_Return_of_material_scrap_item

      tc = '0040'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)

end 

def test_0050_create_mmrn_for_item_status_withdrawn

      tc = '0050'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)

end


def test_0060_create_mmrn_without_store

      tc = '0060'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_failure(tc, test_data, attributes)

end

def test_0070_create_mmrn_without_account_code

      tc = '0070'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_failure(tc, test_data, attributes)
  
end
  
def test_0080_create_mmrn_Quantity_rate_per_unit

      tc = '0080'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_failure(tc, test_data, attributes)

end 

def test_0090_create_mmrn_rate_per_unit_zero

      tc = '0090'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)

end


def test_0091_create_mmrn_supplier_receipt

      tc = '0091'    
      hash_array = get_input_data_oo_hasharray(tc, CREATE_MMRN_SHEET, Stores_constants::INPUT_OO)
      attributes = hash_array[1]
      test_data = hash_array[2] 

      create_item(tc, test_data, attributes)  
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
  
end


def teardown

logout_stores
close_all_windows

rescue Exception => e
puts e

#~ if $browser !=nil
      #~ $browser.close
    #~ end  
    
ensure
Process.kill(9,@pid)
end

end

   
   